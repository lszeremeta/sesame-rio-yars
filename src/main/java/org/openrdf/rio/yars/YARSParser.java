/* 
 * Licensed to Aduna under one or more contributor license agreements.  
 * See the NOTICE.txt file distributed with this work for additional 
 * information regarding copyright ownership. 
 *
 * Aduna licenses this file to you under the terms of the Aduna BSD 
 * License (the "License"); you may not use this file except in compliance 
 * with the License. See the LICENSE.txt file distributed with this work 
 * for the full License.
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.openrdf.rio.yars;

import java.io.*;
import java.util.*;

import org.apache.commons.io.input.BOMInputStream;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RioSetting;
import org.openrdf.rio.helpers.BasicParserSettings;
import org.openrdf.rio.helpers.RDFParserBase;
import org.openrdf.rio.helpers.YARSParserSettings;

/**
 * RDF parser for canonical <a href="https://www.researchgate.net/publication/309695477_RDF_Data_in_Property_Graph_Model">YARS</a>
 * files.
 *
 * @author Łukasz Szeremeta 2017
 */
public class YARSParser extends RDFParserBase {

	/*-----------*
     * Variables *
	 *-----------*/

    private Resource subject;

    private URI predicate;

    private Value object;

    private BufferedReader br;

    private String line;

    private String[] nodes_tab;

    private Map<String, String> hashes = new HashMap<String, String>();

    private String[] relations_tab;

    private String tmp_string;

	/*--------------*
     * Constructors *
	 *--------------*/

    /**
     * Creates a new YARSParser that will use a {@link ValueFactoryImpl} to
     * create RDF model objects.
     */
    public YARSParser() {
        super();
    }

	/*---------*
     * Methods *
	 *---------*/

    public RDFFormat getRDFFormat() {
        return RDFFormat.YARS;
    }

    @Override
    public Collection<RioSetting<?>> getSupportedSettings() {
        Set<RioSetting<?>> result = new HashSet<RioSetting<?>>(super.getSupportedSettings());
        result.add(YARSParserSettings.CASE_INSENSITIVE_DIRECTIVES);
        return result;
    }

    /**
     * Implementation of the <tt>parse(InputStream, String)</tt> method defined
     * in the RDFParser interface.
     *
     * @param in      The InputStream from which to read the data, must not be
     *                <tt>null</tt>. The InputStream is supposed to contain UTF-8 encoded
     *                Unicode characters, as per the YARS specification.
     * @param baseURI The URI associated with the data in the InputStream, must not be
     *                <tt>null</tt>.
     * @throws IOException              If an I/O error occurred while data was read from the InputStream.
     * @throws RDFParseException        If the parser has found an unrecoverable parse error.
     * @throws RDFHandlerException      If the configured statement handler encountered an unrecoverable
     *                                  error.
     * @throws IllegalArgumentException If the supplied input stream or base URI is <tt>null</tt>.
     */
    public synchronized void parse(InputStream in, String baseURI)
            throws IOException, RDFParseException, RDFHandlerException {
        if (in == null) {
            throw new IllegalArgumentException("Input stream must not be 'null'");
        }
        // Note: baseURI will be checked in parse(Reader, String)

        try {
            parse(new InputStreamReader(new BOMInputStream(in, false), "UTF-8"), baseURI);
        } catch (UnsupportedEncodingException e) {
            // Every platform should support the UTF-8 encoding...
            throw new RuntimeException(e);
        }
    }

    /**
     * Implementation of the <tt>parse(Reader, String)</tt> method defined in the
     * RDFParser interface.
     *
     * @param reader  The Reader from which to read the data, must not be <tt>null</tt>.
     * @param baseURI The URI associated with the data in the Reader, must not be
     *                <tt>null</tt>.
     * @throws IOException              If an I/O error occurred while data was read from the InputStream.
     * @throws RDFParseException        If the parser has found an unrecoverable parse error.
     * @throws RDFHandlerException      If the configured statement handler encountered an unrecoverable
     *                                  error.
     * @throws IllegalArgumentException If the supplied reader or base URI is <tt>null</tt>.
     */
    public synchronized void parse(Reader reader, String baseURI)
            throws IOException, RDFParseException, RDFHandlerException {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be 'null'");
        }
        if (baseURI == null) {
            throw new IllegalArgumentException("base URI must not be 'null'");
        }

        if (rdfHandler != null) {
            rdfHandler.startRDF();
        }

        // Store normalized base URI
        setBaseURI(baseURI);

        br = new BufferedReader(reader);

        while ((line = br.readLine()) != null) {
            // check if node definition or relation definition
            if (line.startsWith("(") && line.endsWith("})")) {
                // node definition
                // get hash
                nodes_tab = line.substring(1, line.length() - 3).split("\\{v:'");
                // add to HashMap
                hashes.put(nodes_tab[0], nodes_tab[1]);
            } else if (line.startsWith("(") && !line.endsWith("})") && line.endsWith(")")) {
                // relation definition
                relations_tab = line.substring(1, line.length() - 1).split("(\\)-\\[)|(\\]->\\()");

                // subject
                tmp_string = hashes.get(relations_tab[0]);
                tmp_string = tmp_string.substring(1, tmp_string.length() - 1);
                subject = super.resolveURI(tmp_string);

                // predicate
                tmp_string = relations_tab[1];
                tmp_string.substring(1, tmp_string.length() - 1);
                predicate = super.resolveURI(tmp_string);

                // object
                tmp_string = hashes.get(relations_tab[2]);

                if (tmp_string.charAt(0) == '<') {
                    tmp_string = tmp_string.substring(1, tmp_string.length() - 1);
                    object = super.resolveURI(tmp_string);
                } else {
                    object = createLiteral(tmp_string, null, null);
                }

                reportStatement(subject, predicate, object);
            }
        }

        if (rdfHandler != null) {
            rdfHandler.endRDF();
        }
    }

    protected void reportStatement(Resource subj, URI pred, Value obj)
            throws RDFParseException, RDFHandlerException {
        Statement st = createStatement(subj, pred, obj);
        if (rdfHandler != null) {
            rdfHandler.handleStatement(st);
        }
    }
}
