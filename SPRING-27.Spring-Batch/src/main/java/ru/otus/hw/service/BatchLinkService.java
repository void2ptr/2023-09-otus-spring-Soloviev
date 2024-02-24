package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.model.BatchLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unused")
@Service
public class BatchLinkService {
    private final Map<String, BatchLink> batchLinkMap = new HashMap<>();

    public Object getExportLink(String objectClassName, String objectId) {
        return batchLinkMap.get(objectClassName + '_' + objectId).getExportObject();
    }

    public void setBatchLinkMap(String objectClassName, String objectId, Object exportObject) {
        this.batchLinkMap.put(objectClassName + '_' + objectId, new BatchLink(objectClassName, exportObject));
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getObjects(String objectClassName) {
        List<T> objects = new ArrayList<>();
        batchLinkMap.forEach((k, v) -> {
            if (v.getObjectNameId().contains(objectClassName)) {
                Object obj = v.getObjectNameId();
                objects.add((T) v.getExportObject());
            }
        });
        return objects;
    }

}
