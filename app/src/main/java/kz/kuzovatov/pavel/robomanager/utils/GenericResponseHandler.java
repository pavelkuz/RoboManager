package kz.kuzovatov.pavel.robomanager.utils;

import java.util.List;

import kz.kuzovatov.pavel.robomanager.models.BaseEntity;

public interface GenericResponseHandler<T extends BaseEntity> {
    List<T> getObjectsResponse(String response);
    T getObjectResponse(String response);
}
