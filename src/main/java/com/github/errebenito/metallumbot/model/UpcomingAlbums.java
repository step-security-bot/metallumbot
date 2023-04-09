package com.github.errebenito.metallumbot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;

/**
 * POJO for mapping the JSON.

 * @author rbenito
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"aaData"})
public class UpcomingAlbums implements Serializable {
  
  private static final long serialVersionUID = 7212384018505488798L;

  private static final String PROPERTY_NAME = "aaData";

  @JsonProperty(PROPERTY_NAME)
  @Valid
  private List<List<String>> albumData = new ArrayList<>();

  /**
  * No args constructor for use in serialization.
  *
  */
  public UpcomingAlbums() {
    // Intentionally empty
  }
  
  /**
  * Constructor.

  * @param albumData The upcoming albums data.
  */
  public UpcomingAlbums(final List<List<String>> albumData) {
    super();
    this.albumData = albumData;
  }
    
  @JsonProperty(PROPERTY_NAME)
  public List<List<String>> getAlbumData() {
    return albumData;
  }
  
  @JsonProperty(PROPERTY_NAME)
  public void setalbumData(final List<List<String>> albumData) {
    this.albumData = albumData;
  }  
  
  @Override
  public String toString() {
    final List<String> result = new ArrayList<>();
    for (final List<String> list : this.albumData.subList(0, 10)) {
      for (final String string : list) {
        if (isAlbumLink(string)) {
          result.add(sanitizeLink(string));
        }
      }
    }
    return Arrays.toString(result.toArray()).replace("[", "").replace("]", "");
  }

  private String sanitizeLink(final String link) {
    final int index = link.lastIndexOf("\">");
    return link.substring(8, index).replace("\"", "");
  }
  
  private boolean isAlbumLink(final String string) {
    return string.contains("https://www.metal-archives.com/albums/");
  }

}
