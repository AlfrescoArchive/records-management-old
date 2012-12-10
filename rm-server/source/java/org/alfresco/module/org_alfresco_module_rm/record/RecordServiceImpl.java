/*
 * Copyright (C) 2005-2012 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.module.org_alfresco_module_rm.record;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.model.ContentModel;
import org.alfresco.module.org_alfresco_module_rm.RecordsManagementService;
import org.alfresco.module.org_alfresco_module_rm.identifier.IdentifierService;
import org.alfresco.module.org_alfresco_module_rm.model.RecordsManagementModel;
import org.alfresco.module.org_alfresco_module_rm.security.ExtendedSecurityService;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.repo.security.permissions.AccessDeniedException;
import org.alfresco.service.cmr.dictionary.AspectDefinition;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AccessStatus;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.service.namespace.QName;
import org.alfresco.service.namespace.RegexQNamePattern;
import org.alfresco.util.ParameterCheck;

/**
 * Record service implementation
 * 
 * @author Roy Wetherall
 * @since 2.1
 */
public class RecordServiceImpl implements RecordService, RecordsManagementModel
{
    /** Node service **/
    private NodeService nodeService;

    /** Indentiy service */
    private IdentifierService identifierService;

    /** Dictionary service */
    private DictionaryService dictionaryService;

    /** Permission service */
    private PermissionService permissionService;

    /** Extended security service */
    private ExtendedSecurityService extendedSecurityService;
    
    /** Records management service */
    private RecordsManagementService recordsManagementService;

    /** List of available record meta-data aspects */
    private Set<QName> recordMetaDataAspects;

    /**
     * @param nodeService node service
     */
    public void setNodeService(NodeService nodeService)
    {
        this.nodeService = nodeService;
    }

    /**
     * @param identifierService identifier service
     */
    public void setIdentifierService(IdentifierService identifierService)
    {
        this.identifierService = identifierService;
    }

    /**
     * @param dictionaryService dictionary service
     */
    public void setDictionaryService(DictionaryService dictionaryService)
    {
        this.dictionaryService = dictionaryService;
    }

    /**
     * @param permissionService permission service
     */
    public void setPermissionService(PermissionService permissionService)
    {
        this.permissionService = permissionService;
    }

    /**
     * @param extendedSecurityService   extended security service
     */
    public void setExtendedSecurityService(ExtendedSecurityService extendedSecurityService)
    {
        this.extendedSecurityService = extendedSecurityService;
    }

    /**
     * @param recordsManagementService  records management service
     */
    public void setRecordsManagementService(RecordsManagementService recordsManagementService)
    {
        this.recordsManagementService = recordsManagementService;
    }
    
    /**
     * Init method
     */
    public void init()
    {
        // TODO
    }

    /**
     * @see org.alfresco.module.org_alfresco_module_rm.record.RecordService#getRecordMetaDataAspects()
     */
    @Override
    public Set<QName> getRecordMetaDataAspects()
    {
        if (recordMetaDataAspects == null)
        {
            recordMetaDataAspects = new HashSet<QName>(7);
            Collection<QName> aspects = dictionaryService.getAllAspects();
            for (QName aspect : aspects)
            {
                AspectDefinition def = dictionaryService.getAspect(aspect);
                if (def != null)
                {
                    QName parent = def.getParentName();
                    if (parent != null && ASPECT_RECORD_META_DATA.equals(parent) == true)
                    {
                        recordMetaDataAspects.add(aspect);
                    }
                }
            }
        }
        return recordMetaDataAspects;
    }

    /**
     * @see org.alfresco.module.org_alfresco_module_rm.record.RecordService#isRecord(org.alfresco.service.cmr.repository.NodeRef)
     */
    @Override
    public boolean isRecord(NodeRef nodeRef)
    {
        ParameterCheck.mandatory("nodeRef", nodeRef);

        return nodeService.hasAspect(nodeRef, ASPECT_RECORD);
    }

    /**
     * @see org.alfresco.module.org_alfresco_module_rm.record.RecordService#isDeclared(org.alfresco.service.cmr.repository.NodeRef)
     */
    @Override
    public boolean isDeclared(NodeRef record)
    {
        ParameterCheck.mandatory("record", record);

        return nodeService.hasAspect(record, ASPECT_DECLARED_RECORD);
    }
    
    /**
     * @see org.alfresco.module.org_alfresco_module_rm.record.RecordService#isFiled(org.alfresco.service.cmr.repository.NodeRef)
     */
    @Override
    public boolean isFiled(NodeRef nodeRef)
    {
        ParameterCheck.mandatory("nodeRef", nodeRef);
        
        boolean result = false;
        
        if (isRecord(nodeRef) == true)
        {
            ChildAssociationRef childAssocRef = nodeService.getPrimaryParent(nodeRef);
            if (childAssocRef != null)
            {
                NodeRef parent = childAssocRef.getParentRef();
                if (parent != null && 
                    recordsManagementService.isRecordFolder(parent) == true)
                {
                    result = true;
                }
            }
        }
        
        return result;
    }

    /**
     * @see org.alfresco.module.org_alfresco_module_rm.record.RecordService#getUnfiledRootContainer(org.alfresco.service.cmr.repository.NodeRef)
     */
    @Override
    public NodeRef getUnfiledContainer(NodeRef filePlan)
    {
        ParameterCheck.mandatory("filePlan", filePlan);
        
        if (recordsManagementService.isFilePlan(filePlan) == false)
        {
            throw new AlfrescoRuntimeException("Unable to get the unfiled container, because passed node is not a file plan.");
        }

        NodeRef result = null;
        
        List<ChildAssociationRef> assocs = nodeService.getChildAssocs(filePlan, ASSOC_UNFILED_RECORDS,
                RegexQNamePattern.MATCH_ALL);
        if (assocs.size() > 1) 
        { 
            throw new AlfrescoRuntimeException(
                "Unable to get the unfiled container, because the container cannot be indentified."); 
        }
        else if (assocs.size() == 1 )
        {    
            result = assocs.get(0).getChildRef();
        }
        
        return result;
    }

    /**
     * @see org.alfresco.module.org_alfresco_module_rm.record.RecordService#createRecord(org.alfresco.service.cmr.repository.NodeRef,
     *      org.alfresco.service.cmr.repository.NodeRef)
     */
    @Override
    public void createRecord(final NodeRef filePlan, final NodeRef nodeRef)
    {
        ParameterCheck.mandatory("filePlan", filePlan);
        ParameterCheck.mandatory("document", nodeRef);
        
        if (nodeService.hasAspect(nodeRef, ASPECT_RECORD) == false)
        {        
            // first we do a sanity check to ensure that the user has at least write permissions on the document
            if (permissionService.hasPermission(nodeRef, PermissionService.WRITE) != AccessStatus.ALLOWED)
            {
                throw new AccessDeniedException("Can not create record from document, because the user " + 
                                                AuthenticationUtil.getFullyAuthenticatedUser() +
                                                " does not have Write permissions on the doucment " +
                                                nodeRef.toString());
            }
            
            // do the work of creating the record as the system user
            AuthenticationUtil.runAsSystem(new RunAsWork<Void>() 
            {
    
                @Override
                public Void doWork() throws Exception
                {
                    // get the new record container for the file plan
                    NodeRef newRecordContainer = getUnfiledContainer(filePlan);
                    if (newRecordContainer == null) 
                    { 
                        throw new AlfrescoRuntimeException("Unable to create record, because new record container could not be found."); 
                    }
                                        
                    // get the documents readers
                    Long aclId = nodeService.getNodeAclId(nodeRef);
                    Set<String> readers = permissionService.getReaders(aclId);
    
                    // get the documents primary parent assoc
                    ChildAssociationRef parentAssoc = nodeService.getPrimaryParent(nodeRef);
    
                    // move the document into the file plan
                    nodeService.moveNode(nodeRef, newRecordContainer, ContentModel.ASSOC_CONTAINS, parentAssoc.getQName());
    
                    // maintain the original primary location
                    nodeService.addChild(parentAssoc.getParentRef(), nodeRef, parentAssoc.getTypeQName(), parentAssoc.getQName());
    
                    // make the document a record
                    makeRecord(nodeRef);
    
                    // set the readers
                    extendedSecurityService.setExtendedReaders(nodeRef, readers);
                    
                    return null;
                }
            });
        }
    }

    /**
     * @see org.alfresco.module.org_alfresco_module_rm.record.RecordService#fileRecord(org.alfresco.service.cmr.repository.NodeRef, org.alfresco.service.cmr.repository.NodeRef)
     */
    @Override
    public void fileRecord(NodeRef record, NodeRef recordFolder)
    {
        // check if this is a record
        if (isRecord(record) == false)
        {
            // TODO .. should we make this a record?
            throw new UnsupportedOperationException("Currently unable to file something that isn't already a record");
        }
        
        if (isFiled(record) == false)
        {
            // TODO .. refactor the existing code to file a record here ... this will include moving the code that
            //         currently manages the properties of the disposition lifecycle aspect into the disposition service
            throw new UnsupportedOperationException("Currently unsuported.");
        }
        else
        {
            // TODO .. figure out how we 'refile' a currently filed record
            throw new UnsupportedOperationException("Currently unable to file an already filed record.");
        }
        
    }
    
    /**
     * Helper Methods
     */

    /**
     * Creates a record from the given document
     * 
     * @param document the document from which a record will be created
     */
    private void makeRecord(NodeRef document)
    {
        nodeService.addAspect(document, RecordsManagementModel.ASPECT_RECORD, null);

        String recordId = identifierService.generateIdentifier(ASPECT_RECORD, nodeService.getPrimaryParent(document)
                .getParentRef());
        nodeService.setProperty(document, PROP_IDENTIFIER, recordId);
    }
}
