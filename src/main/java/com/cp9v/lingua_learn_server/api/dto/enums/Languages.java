package com.cp9v.lingua_learn_server.api.dto.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Languages {
    RU("Russian", "ru"),
    EN("English", "en"),
    CZ("Czech", "cz"),;

    public final String longName;
    public final String shortName;
}
