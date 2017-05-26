package org.cloudgarden.sotagparser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by sergey on 27.05.17.
 */
@Data
@NoArgsConstructor
public class Question {
    private List<String> tags;
}
