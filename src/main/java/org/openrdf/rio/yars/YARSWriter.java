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

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.helpers.RDFWriterBase;

/**
 * An implementation of the RDFWriter interface that writes RDF documents in
 * canonical YARS format. The YARS format is defined in <a
 * href="https://www.researchgate.net/publication/309695477_RDF_Data_in_Property_Graph_Model">in this document</a>.
 *
 * @author Łukasz Szeremeta 2017
 */
public class YARSWriter extends RDFWriterBase implements RDFWriter {
	@Override
	public RDFFormat getRDFFormat() {
		return null;
	}

	@Override
	public void startRDF() throws RDFHandlerException {

	}

	@Override
	public void endRDF() throws RDFHandlerException {

	}

	@Override
	public void handleStatement(Statement statement) throws RDFHandlerException {

	}

	@Override
	public void handleComment(String s) throws RDFHandlerException {

	}
}
