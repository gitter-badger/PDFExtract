/*
 * Copyright 2010 Øyvind Berg (elacin@gmail.com)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.elacin.pdfextract.tree;


import org.apache.log4j.Logger;
import org.elacin.pdfextract.geom.HasPosition;
import org.elacin.pdfextract.logical.text.Role;
import org.elacin.pdfextract.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: elacin Date: Mar 23, 2010 Time: 7:44:33 AM To change this
 * template use File | Settings | File Templates.
 */
public abstract class AbstractNode<ParentType extends AbstractParentNode> implements HasPosition {
// ------------------------------ FIELDS ------------------------------

protected static final Logger log = Logger.getLogger(AbstractNode.class);
protected Map<Role, String> roles;
protected ParentType        parent;
protected DocumentNode      root;

/* a cache of current text */
@Nullable
protected String textCache;

/* a cache of current toString-String */
@Nullable
protected transient String toStringCache;

// --------------------- GETTER / SETTER METHODS ---------------------

public ParentType getParent() {
    return parent;
}

public DocumentNode getRoot() {
    return root;
}

public void setRoot(final DocumentNode root) {
    this.root = root;
}

// -------------------------- PUBLIC METHODS --------------------------

public void addRole(Role r, String reason) {
    log.warn(this + " got assigned role " + r);
    if (roles == null) {
        roles = new EnumMap<Role, String>(Role.class);
    }
    roles.put(r, reason);
}

@Nullable
public PageNode getPage() {
    AbstractNode current = this;
    while (current != null) {
        if (current instanceof PageNode) {
            return (PageNode) current;
        }
        current = current.parent;
    }
    return null;
}

@NotNull
public Set<Role> getRoles() {
    if (roles == null) {
        return null;
    }
    return roles.keySet();
}

public abstract Style getStyle();

public abstract String getText();

public boolean hasRole(Role r) {
    if (roles == null) {
        return false;
    }

    return roles.containsKey(r);
}

// -------------------------- OTHER METHODS --------------------------

protected abstract void invalidateThisAndParents();
}