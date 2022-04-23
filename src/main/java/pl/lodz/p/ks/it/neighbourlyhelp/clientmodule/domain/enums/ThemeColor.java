package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ThemeColor {
    LIGHT("LIGHT"),
    DARK("DARK");

    private final String value;
}