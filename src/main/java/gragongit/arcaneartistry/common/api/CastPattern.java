package gragongit.arcaneartistry.common.api;

import java.util.List;
import gragongit.arcaneartistry.common.staff.StaffDirection;

public record CastPattern(List<StaffDirection> pattern) {

  @Override
  public boolean equals(Object obj) {
    return pattern.equals(obj);
  }
}
