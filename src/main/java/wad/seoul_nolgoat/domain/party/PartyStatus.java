package wad.seoul_nolgoat.domain.party;

import wad.seoul_nolgoat.exception.ApplicationException;

import static wad.seoul_nolgoat.exception.ErrorCode.INVALID_PARTY_STATUS;

public enum PartyStatus {
    OPENED,
    CLOSED;

    public static PartyStatus fromString(String status) {
        if (status == null) {
            return null;
        }
        try {
            return PartyStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(INVALID_PARTY_STATUS);
        }
    }
}
