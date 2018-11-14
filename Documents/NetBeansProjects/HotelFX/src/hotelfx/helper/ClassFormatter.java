/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import com.jfoenix.controls.JFXTextField;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;

/**
 *
 * @author ANDRI
 */
public class ClassFormatter {

    public static final String upperAlphabeth = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lowerAlphabeth = upperAlphabeth.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    public static final String alphanum = upperAlphabeth + lowerAlphabeth + digits;

//    public static final DecimalFormat currencyFormat = new DecimalFormat("#,###");
//    public static final CDecimalFormat currencyFormat = new CDecimalFormat("#,##0.00");
    public static final CDecimalFormat currencyFormat = new CDecimalFormat("#,###.##");

//    public static final CDecimalFormat decimalFormat = new CDecimalFormat("#0.00");
    public static final CDecimalFormat decimalFormat = new CDecimalFormat("#,###.##");

//    public static final String datePartern = "dd/MM/yyyy";
    public static final String datePartern = "dd MMM yyyy";

    public static final String timePartern = "HH:mm";

    public static final String dateTimePartern = datePartern + " " + timePartern;

    public static final SimpleDateFormat dateFormate = new SimpleDateFormat(datePartern);

    public static final SimpleDateFormat timeFormate = new SimpleDateFormat(timePartern);

    public static final SimpleDateFormat dateTimeFormate = new SimpleDateFormat(dateTimePartern);

    public static void setDatePickersPattern(String pattern, DatePicker... datePickers) {
        for (DatePicker datePicker : datePickers) {
            datePicker.setConverter(new StringConverter<LocalDate>() {

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            });
        }
    }

    public static void setDatePickersEnableDateFrom(LocalDate beginEnableDate, DatePicker... datePickers) {
        for (DatePicker datePicker : datePickers) {
            datePicker.setDayCellFactory((DatePicker param) -> new DateDisable(beginEnableDate, null));
        }
    }

    public static void setDatePickersEnableDateUntil(LocalDate endEnableDate, DatePicker... datePickers) {
        for (DatePicker datePicker : datePickers) {
            datePicker.setDayCellFactory((DatePicker param) -> new DateDisable(null, endEnableDate));
        }
    }
    
    public static void setDatePickersEnableDate(LocalDate beginEnableDate, LocalDate endEnableDate, DatePicker... datePickers) {
        for (DatePicker datePicker : datePickers) {
            datePicker.setDayCellFactory((DatePicker param) -> new DateDisable(beginEnableDate, endEnableDate));
        }
    }

    public static class DateDisable extends DateCell {

        private final LocalDate beginEnableDate;

        private final LocalDate endEnableDate;

        public DateDisable(LocalDate beginEnableDate, LocalDate endEnableDate) {
            this.beginEnableDate = beginEnableDate;
            this.endEnableDate = endEnableDate;
        }

        @Override
        public void updateItem(LocalDate item, boolean empty) {

            super.updateItem(item, empty);
            if (beginEnableDate != null && endEnableDate != null) {
                if (item.isBefore(beginEnableDate)
                        || item.isAfter(endEnableDate)) {
                    setDisable(true);
                }
            } else {
                if (beginEnableDate != null) {
                    if (item.isBefore(beginEnableDate)) {
                        setDisable(true);
                    }
                } else {
                    if (endEnableDate != null) {
                        if (item.isAfter(endEnableDate)) {
                            setDisable(true);
                        }
                    }
                }
            }
        }
    }

    //covert name day in  English to Indonesia
    public static String convertDayToIndonesian(int i) {
        String result = "";
        switch (i) {
            case 1:
                result = "Senin";
                break;
            case 2:
                result = "Selasa";
                break;
            case 3:
                result = "Rabu";
                break;
            case 4:
                result = "Kamis";
                break;
            case 5:
                result = "Jumat";
                break;
            case 6:
                result = "Sabtu";
                break;
            case 7:
                result = "Minggu";
                break;
            default:
                break;
        }
        return result;
    }

    //dont forget to plus 1?
    public static String convertMonthToIndonesian(int i) {
        String month = "";
        switch (i) {
            case 1:
                month = "Januari";
                break;
            case 2:
                month = "Februari";
                break;
            case 3:
                month = "Maret";
                break;
            case 4:
                month = "April";
                break;
            case 5:
                month = "Mei";
                break;
            case 6:
                month = "Juni";
                break;
            case 7:
                month = "Juli";
                break;
            case 8:
                month = "Agustus";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "Oktober";
                break;
            case 11:
                month = "November";
                break;
            case 12:
                month = "Desember";
                break;
            default:
                break;
        }
        return month;
    }

    public static double toDoubleWithXDecimal(double doubleValue, int decimalDigit) {
        return Math.round(doubleValue * (Math.pow(10, decimalDigit))) / (Math.pow(10, decimalDigit));
    }

    /**
     * Custom - Decimal Format
     */
    public static class CDecimalFormat extends DecimalFormat {

        public CDecimalFormat(String format) {
            super(format);
        }

        public String cFormat(Object obj) {
            if (obj != null) {
                return format(obj);
            } else {
                return format(0);
            }
        }

        public String cFormat(Object obj, String nullValue) {
            if (obj != null) {
                return format(obj);
            } else {
                return nullValue;
            }
        }

    }

    /**
     * Custom - BigDecimalStringConverter
     */
    public static class CBigDecimalStringConverter extends BigDecimalStringConverter {

        public CBigDecimalStringConverter() {

        }

        @Override
        public BigDecimal fromString(String value) {
            // If the specified value is null or zero-length, return null
            if (value == null) {
                return null;
            }

            value = value.trim();

            if (value.length() < 1) {
                return null;
            }

            //custom
            if (!isNumeric(value, "BigDecimal")) {
                return null;
            }

            return new BigDecimal(value);
        }

        @Override
        public String toString(BigDecimal value) {
            // If the specified value is null, return a zero-length String
            if (value == null) {
                return "";
            }

            return ((BigDecimal) value).stripTrailingZeros().toPlainString();
        }

    }

    public static boolean isNumeric(String str, String numbericType) {
        switch (numbericType) {
            case "Double":
                try {
                    Double d = Double.parseDouble(str);
                } catch (NumberFormatException nfe) {
                    return false;
                }
                return true;
            case "BigDecimal":
                try {
                    BigDecimal bd = new BigDecimal(str);
                } catch (NumberFormatException nfe) {
                    return false;
                }
                return true;
            case "Integer":
                try {
                    Integer i = Integer.parseInt(str);
                } catch (NumberFormatException nfe) {
                    return false;
                }
                return true;
            case "Long":
                try {
                    Long l = Long.parseLong(str);
                } catch (NumberFormatException nfe) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public static void setToNumericField(String numbericType, Node... fields) {
        for (Node field : fields) {
            if (field instanceof JFXTextField) {
                ((JFXTextField) field).textProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null
                            && !newVal.equals("")) {
                        if (newVal.equals("-")
                                && newVal.length() == 1) {
                            //do nothing
                        } else {
                            if (!ClassFormatter.isNumeric(newVal, numbericType)) {
                                ((JFXTextField) field).setText(oldVal);
                            }
                        }
                    }
                });
            }
            if (field instanceof TextField) {
                ((TextField) field).textProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null
                            && !newVal.equals("")) {
                        if (newVal.equals("-")
                                && newVal.length() == 1) {
                            //do nothing
                        } else {
                            if (!ClassFormatter.isNumeric(newVal, numbericType)) {
                                ((TextField) field).setText(oldVal);
                            }
                        }
                    }
                });
            }
        }
    }

}
