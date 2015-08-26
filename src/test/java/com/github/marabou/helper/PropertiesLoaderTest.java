/**
 * Marabou - Audio Tagger
 * <p>
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 * <p>
 * https://github.com/hennr/marabou
 * <p>
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.helper;

import com.github.marabou.properties.PropertiesLoader;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.Properties;

public class PropertiesLoaderTest {


    @Test
    public void testLoadProperties() throws Exception {

        // given
        PropertiesLoader propertiesLoader = new PropertiesLoader(new PathHelper());
        String propertiesContent = "foo=bar";
        Properties properties = propertiesLoader.loadProperties(new ByteArrayInputStream(propertiesContent.getBytes()));

        // then
        assertEquals("bar", properties.getProperty("foo"));
    }
}
