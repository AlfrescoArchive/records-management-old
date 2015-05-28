/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
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
package org.alfresco.po.common;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.tuple.Pair;

/**
 * A helper class containing utility methods for dealing with {@link Stream}s.
 *
 * @author tpage
 * @since 3.0
 */
public class StreamHelper
{
    /**
     * Zip two streams into a {@code Stream} of {@link Pair}s. Based on the code here:
     * http://stackoverflow.com/a/23529010 which in turn was based on
     * http://download.java.net/lambda/b93/docs/api/java/util/stream/Streams.html
     *
     * @param a The stream whose elements should go on the left of each pair.
     * @param b The stream whose elements should go on the right of each pair.
     * @return A stream of paired elements.
     */
    public static <A, B> Stream<Pair<A, B>> zip(Stream<? extends A> a, Stream<? extends B> b)
    {
        @SuppressWarnings("unchecked")
        Spliterator<A> aSpliterator = (Spliterator<A>) Objects.requireNonNull(a).spliterator();
        @SuppressWarnings("unchecked")
        Spliterator<B> bSpliterator = (Spliterator<B>) Objects.requireNonNull(b).spliterator();

        // Zipping loses DISTINCT and SORTED characteristics
        int both = aSpliterator.characteristics() & bSpliterator.characteristics()
                    & ~(Spliterator.DISTINCT | Spliterator.SORTED);
        int characteristics = both;

        long zipSize = ((characteristics & Spliterator.SIZED) != 0) ? Math.min(aSpliterator.getExactSizeIfKnown(),
                    bSpliterator.getExactSizeIfKnown()) : -1;

        Iterator<A> aIterator = Spliterators.iterator(aSpliterator);
        Iterator<B> bIterator = Spliterators.iterator(bSpliterator);
        Iterator<Pair<A, B>> cIterator = new Iterator<Pair<A, B>>()
        {
            @Override
            public boolean hasNext()
            {
                return aIterator.hasNext() && bIterator.hasNext();
            }

            @Override
            public Pair<A, B> next()
            {
                return Pair.of(aIterator.next(), bIterator.next());
            }
        };

        Spliterator<Pair<A, B>> split = Spliterators.spliterator(cIterator, zipSize, characteristics);
        return (a.isParallel() || b.isParallel()) ? StreamSupport.stream(split, true) : StreamSupport.stream(split,
                    false);
    }
}
