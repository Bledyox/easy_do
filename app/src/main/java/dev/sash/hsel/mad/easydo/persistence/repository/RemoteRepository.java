package dev.sash.hsel.mad.easydo.persistence.repository;

import dev.sash.hsel.mad.easydo.model.Credentials;

public interface RemoteRepository extends LocalRepository {
    boolean verify(Credentials credentials);
}
