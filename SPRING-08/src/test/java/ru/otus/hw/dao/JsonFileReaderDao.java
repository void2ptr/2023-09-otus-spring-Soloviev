package ru.otus.hw.dao;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.core.io.ClassPathResource;
import ru.otus.hw.dao.exceptions.FileReadException;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonFileReaderDao {
    public static <T> List<T> readCollectionFromFile(String filename, Type listType) {
        List<T> collections;
        ClassPathResource resource = new ClassPathResource(filename);

        try (InputStreamReader streamReader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
        {
            JsonArray jsonArray = JsonParser.parseReader(streamReader).getAsJsonArray();
            collections = new Gson().fromJson(jsonArray, listType);

        } catch (Exception e) {
            throw new FileReadException("Problem to read authors: ", e);
        }

        return collections;
    }
}
