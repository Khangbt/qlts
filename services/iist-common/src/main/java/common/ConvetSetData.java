package common;

import java.lang.reflect.Field;

public class ConvetSetData {
    private static Object xetData(Object entityl, Object dto) {
        Field[] fields = entityl.getClass().getDeclaredFields();
        Field[] fieldsDto = dto.getClass().getDeclaredFields();
        for (Field field : fieldsDto) {
            for (Field field1 : fields) {
                if (field1.getName().equals(field.getName()) && (field.getType().equals(field1.getType()))) {
                    try {
                        field1.setAccessible(true);
                        field.setAccessible(true);
                        field1.set(entityl, field.get(dto));

                    } catch (IllegalAccessException e) {
                        return null;
                    }

                }
            }
        }
        return entityl;
    }
}
