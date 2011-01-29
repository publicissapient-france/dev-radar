package com.xebia.devradar;

import java.util.Set;

public interface Pollable {

    Set<Event> fetch();
}
