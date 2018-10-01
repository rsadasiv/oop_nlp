/********************************************************************************
 * MIT JSemcor Library (JSemcor) v1.0.1
 * Copyright (c) 2008-2011 Massachusetts Institute of Technology
  * 
 * This program and the accompanying materials are made available by MIT under 
 * the terms of the MIT JSemcor License. Refer to the license document included 
 * with this distribution, or contact markaf@alum.mit.edu for further details.
 *******************************************************************************/

package edu.mit.jsemcor.term;

import edu.mit.jsemcor.element.IWordform;

/**
 * Indicates this object represents a note that may be attached to an
 * {@link IWordform} object. A may contain a string that provides additional
 * information about the specific note type, or may be empty.
 * 
 * @author M.A. Finlayson
 * @version 1.59, 22 Sep 2008
 * @since JSemcor 1.0.0
 */
public interface INote extends ITerminal {

	/**
	 * The attribute tag used in SGML-like format context files for these
	 * objects.
	 * 
	 * @since JSemcor 1.0.0
	 */
	public static final String ATTR = "note";

	/**
	 * Returns the note type for this note.
	 * 
	 * @since JSemcor 1.0.0
	 */
	public INoteType getType();

}
