import {t} from "i18next";

export default function getDayName(date: number): string {
    switch (date) {
        case 1:
            return t("TimeTable.day1");
        case 2:
            return t("TimeTable.day2");
        case 3:
            return t("TimeTable.day3");
        case 4:
            return t("TimeTable.day4");
        case 5:
            return t("TimeTable.day5");
        case 6:
            return t("TimeTable.day6");
        case 7:
            return t("TimeTable.day7");
        default:
            return "-";
    }
}