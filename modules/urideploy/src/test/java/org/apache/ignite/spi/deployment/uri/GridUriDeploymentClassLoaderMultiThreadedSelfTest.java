/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.spi.deployment.uri;

import java.net.URL;
import org.apache.ignite.internal.util.typedef.internal.U;
import org.apache.ignite.testframework.config.GridTestProperties;
import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.junit.Test;

/**
 * Grid URI deployment class loader self test.
 */
public class GridUriDeploymentClassLoaderMultiThreadedSelfTest extends GridCommonAbstractTest {
    /**
     * @throws Exception If failed.
     */
    @Test
    public void testMultiThreadedClassLoading() throws Exception {
        for (int i = 0; i < 50; i++)
            doTest();
    }

    /**
     * @throws Exception If failed.
     */
    private void doTest() throws Exception {
        final GridUriDeploymentClassLoader ldr = new GridUriDeploymentClassLoader(
            new URL[] { U.resolveIgniteUrl(GridTestProperties.getProperty("ant.urideployment.gar.file")) },
                getClass().getClassLoader());

        multithreaded(
            () -> {
                ldr.loadClass("org.apache.ignite.spi.deployment.uri.tasks.GridUriDeploymentTestTask0");

                return null;
            },
            500
        );

        final GridUriDeploymentClassLoader ldr0 = new GridUriDeploymentClassLoader(
            new URL[] { U.resolveIgniteUrl(GridTestProperties.getProperty("ant.urideployment.gar.file")) },
            getClass().getClassLoader());

        multithreaded(
            () -> {
                ldr0.loadClassIsolated("org.apache.ignite.spi.deployment.uri.tasks.GridUriDeploymentTestTask0");

                return null;
            },
            500
        );
    }
}
