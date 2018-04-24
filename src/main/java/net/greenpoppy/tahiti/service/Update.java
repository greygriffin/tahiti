package net.greenpoppy.tahiti.service;

import lombok.Value;

// Holder for update which may be setting value to null
// Similar to Optional, but IntelliJ Idea doesn't like you using Optional
@Value
public class Update<T> {
    T value;
}
