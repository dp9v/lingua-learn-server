package com.cp9v.lingua_learn_server.api.dto.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Languages {
    RU("Russian"),
    EN("English"),
    CZ("Czech"),
    ;

    public final String longName;
}
