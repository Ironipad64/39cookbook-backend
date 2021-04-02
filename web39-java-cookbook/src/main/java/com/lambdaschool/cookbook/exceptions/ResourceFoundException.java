package com.lambdaschool.cookbook.exceptions;


/**
 * A custom exception to be used when a resource is found but is not suppose to be
 */
public class ResourceFoundException
        extends RuntimeException {
    public ResourceFoundException(String message) {
        super("ERROR from Cookbooks App ——— " + message);
    }
}
