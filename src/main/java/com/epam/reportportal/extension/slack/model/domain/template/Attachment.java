package com.epam.reportportal.extension.slack.model.domain.template;

import java.util.List;

public class Attachment {

  private String color;
  private List<Block> blocks;

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public List<Block> getBlocks() {
    return blocks;
  }

  public void setBlocks(List<Block> blocks) {
    this.blocks = blocks;
  }
}
