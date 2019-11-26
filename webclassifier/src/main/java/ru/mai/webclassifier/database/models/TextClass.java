package ru.mai.webclassifier.database.models;

public enum TextClass {
    ARMY,
    CULTURE,
    ECONOMY,
    INCIDENTS,
    POLICY,
    SOCIETY,
    SPORT,
    WORLD;
    
    public static String toRus(TextClass textClass) {
        switch (textClass) {
            case ARMY:
                return "Оборона и безопасность";
            case CULTURE:
                return "Культура";
            case ECONOMY:
                return "Экономика";
            case INCIDENTS:
                return "Происществия";
            case POLICY:
                return "Политика";
            case SOCIETY:
                return "Общество";
            case SPORT:
                return "Спорт";
            case WORLD:
                return "В мире";
            default:
                return "Неизвестный класс";
        }
    }
    
}
