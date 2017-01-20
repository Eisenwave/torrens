/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.grian.torrens.nbt;

import net.grian.torrens.io.FileSyntaxException;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * A class which contains NBT-related utility methods.
 *
 */
public final class NBTUtils {

    public static final Charset CHARSET = Charset.forName("UTF-8");

    private NBTUtils() {}

    /**
     * Get child tag of a NBT structure.
     *
     * @param items the map to read from
     * @param key the key to look for
     * @param expected the expected NBT class type
     * @return child tag
     * @throws FileSyntaxException if an I/O error occurs
     */
    public static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws FileSyntaxException {
        if (!items.containsKey(key)) {
            throw new FileSyntaxException("Missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new FileSyntaxException(key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }

}
