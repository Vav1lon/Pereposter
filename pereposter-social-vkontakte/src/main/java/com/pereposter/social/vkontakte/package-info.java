@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(type = DateTime.class,
                value = DateTimeAdapter.class),
        @XmlJavaTypeAdapter(type = DateMidnight.class,
                value = DateMidnightAdapter.class),
        @XmlJavaTypeAdapter(type = LocalDate.class,
                value = LocalDateAdapter.class),
        @XmlJavaTypeAdapter(type = LocalTime.class,
                value = LocalTimeAdapter.class),
        @XmlJavaTypeAdapter(type = LocalDateTime.class,
                value = LocalDateTimeAdapter.class)
})
package com.pereposter.social.vkontakte;

import com.pereposter.utils.jaxb.*;
import org.joda.time.*;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;