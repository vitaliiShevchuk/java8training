package com.serena.datetime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalQueries;
import java.util.*;

public class DateTimeExamples {

    private static Logger LOG = LogManager.getLogger(DateTimeExamples.class);

    @Test
    public void localDates() {

        LocalDate currentDate = LocalDate.now();

        LOG.info(currentDate);

        LOG.info(LocalDate.of(2015, Month.JULY, 16));
        LOG.info(LocalDate.of(2022, 12, 16));
        LOG.info(LocalDate.ofYearDay(2030, 77));

        LOG.info(currentDate.isSupported(ChronoUnit.HOURS));
        LOG.info(currentDate.isSupported(ChronoField.SECOND_OF_DAY));
    }

    @Test
    public void localTimes() {
        LocalTime currentTime = LocalTime.now();

        LOG.info(currentTime);
        LOG.info(LocalTime.of(18, 0, 0));

        //constuct
        LOG.info(currentTime.atDate(LocalDate.now()));
    }

    @Test
    public void localDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();

        LOG.info(localDateTime);

        ZonedDateTime laDateTime = localDateTime.atZone(ZoneId.of("America/Los_Angeles"));
        LOG.info(laDateTime);
    }

    @Test
    public void getSomeInformation() {
        LocalDate date = LocalDate.of(2010, 2, 16);
        LOG.info(LocalDate.now().isBefore(date));
        LOG.info(LocalDate.now().isAfter(date));

        LOG.info(date.isLeapYear());

        Month month = date.getMonth();
        LOG.info(month);
        LOG.info(month.minLength());
        LOG.info(month.maxLength());

        Month firstMonthOfQuarter = month.firstMonthOfQuarter();
        LOG.info(firstMonthOfQuarter);

        LocalDateTime atStartOfDay = date.atStartOfDay();
        LOG.info(atStartOfDay);


        Year currentYear = Year.now();
        Year y2012 = Year.of(2012);

        LOG.info(y2012);
        LOG.info(currentYear.isLeap());

        ZonedDateTime zonedDateTime = Year
                .of(2012)
                .atMonth(Month.DECEMBER)
                .atDay(12)
                .atTime(14, 0)
                .atZone(ZoneId.of("GMT"));
        LOG.info(zonedDateTime);

        LocalTime localTime = TemporalQueries.localTime().queryFrom(zonedDateTime);
        LOG.info(localTime);
    }

    @Test
    public void addSubstract() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LOG.info(tomorrow);

        LocalDateTime dateTime = LocalDateTime.now().minusHours(4).minusMinutes(20);
        LOG.info(dateTime);

        LocalDate localDate = LocalDate.of(2014, Month.FEBRUARY, 1);
        LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        LOG.info(lastDayOfMonth);
    }

    @Test
    public void timeZones() {
        ZoneId losAngeles = ZoneId.of("America/Los_Angeles");
        ZoneId utc = ZoneId.of("UTC");

        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime utcNow = ZonedDateTime.of(now, utc);
        ZonedDateTime losAngelesNow = ZonedDateTime.of(now, losAngeles);
        LOG.info(utc);
        LOG.info(losAngelesNow);

        ZoneOffset offset = ZoneOffset.ofHours(5);
        OffsetDateTime offsetDateTime = OffsetDateTime.of(now, offset);
        LOG.info(offsetDateTime);

//        ZoneId
//                .getAvailableZoneIds().stream()
//                .map(ZoneId::of)
//                .forEach(zone -> LOG.info(now.atZone(zone)));
    }

    @Test
    public void instant() throws InterruptedException {
        Instant timestamp = Instant.now();

        LOG.info(timestamp);
        LOG.info(timestamp.get(ChronoField.NANO_OF_SECOND));
        LOG.info(timestamp.get(ChronoField.MICRO_OF_SECOND));
        LOG.info(timestamp.get(ChronoField.MILLI_OF_SECOND));
    }

    @Test
    public void periodsAndDurations() {
        LocalDate firstDate = LocalDate.of(2012, 5, 17);
        LocalDate secondDate = LocalDate.of(2020, 3, 7);

        //can work only with local dates
        Period period = Period.between(firstDate, secondDate);
        LOG.info(period);
        LOG.info(period.getMonths());


        ZonedDateTime gmtTimeNow = ZonedDateTime.now(ZoneId.of("GMT"));
        ZonedDateTime utcTime = ZonedDateTime.now(ZoneId.of("UTC"))
                .minusHours(47);

        Duration duration = Duration.between(gmtTimeNow, utcTime);
        LOG.info(duration);
        LOG.info(duration.getSeconds());
    }

    @Test
    public void formatAndParse() {
        LocalDateTime dateTime = LocalDateTime.of(2014, Month.FEBRUARY, 1, 10, 45);

        String asBasicIsoDate = dateTime.format(DateTimeFormatter.BASIC_ISO_DATE);
        String asIsoWeekDate = dateTime.format(DateTimeFormatter.ISO_WEEK_DATE);
        String asIsoDateTime = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);

        LOG.info("{} asBasicIsoDate {}", dateTime.toString(), asBasicIsoDate);
        LOG.info("{} asIsoWeekDate {}", dateTime.toString(), asIsoWeekDate);
        LOG.info("{} asIsoDateTime {}", dateTime.toString(), asIsoDateTime);


        String asCustomPattern = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofLocalizedDateTime(FormatStyle.SHORT)
                        .withLocale(Locale.GERMANY);
        String germanDateTime = dateTime.format(formatter);


        LocalDate fromIsoDate = LocalDate.parse("2014-01-20");
        LocalDate fromIsoWeekDate = LocalDate.parse("2014-W14-2", DateTimeFormatter.ISO_WEEK_DATE);
        LocalDate fromCustomPattern = LocalDate.parse("20.01.2014", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    public void conversions() {
        // LocalDate/LocalTime <-> LocalDateTime
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalDateTime dateTimeFromDateAndTime = LocalDateTime.of(date, time);
        LocalDate dateFromDateTime = LocalDateTime.now().toLocalDate();
        LocalTime timeFromDateTime = LocalDateTime.now().toLocalTime();

        // Instant <-> LocalDateTime
        Instant instant = Instant.now();
        LocalDateTime dateTimeFromInstant = LocalDateTime.ofInstant(instant, ZoneId.of("America/Los_Angeles"));
        Instant instantFromDateTime = LocalDateTime.now().toInstant(ZoneOffset.ofHours(-2));

        // convert old date/calendar/timezone classes
        Instant instantFromDate = new Date().toInstant();
        Instant instantFromCalendar = Calendar.getInstance().toInstant();
        ZoneId zoneId = TimeZone.getDefault().toZoneId();
        ZonedDateTime zonedDateTimeFromGregorianCalendar = new GregorianCalendar().toZonedDateTime();
// convert to old classes
        Date dateFromInstant = Date.from(Instant.now());
        TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("America/Los_Angeles"));
        GregorianCalendar gregorianCalendar = GregorianCalendar.from(ZonedDateTime.now());
    }

}