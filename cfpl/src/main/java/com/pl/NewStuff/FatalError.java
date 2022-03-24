package com.pl.NewStuff;


public class FatalError extends Exception {
    FatalError (String message) {
        super (message);
    }
    FatalError () {
        super ();
    }
}
