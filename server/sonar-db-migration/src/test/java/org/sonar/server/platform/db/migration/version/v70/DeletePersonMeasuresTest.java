/*
 * SonarQube
 * Copyright (C) 2009-2019 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.platform.db.migration.version.v70;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Rule;
import org.junit.Test;
import org.sonar.db.CoreDbTester;
import org.sonar.server.platform.db.migration.step.DataChange;

import static org.assertj.core.api.Assertions.assertThat;

public class DeletePersonMeasuresTest {
  private static final AtomicInteger GENERATOR = new AtomicInteger();

  @Rule
  public CoreDbTester db = CoreDbTester.createForSchema(DeletePersonMeasuresTest.class, "initial.sql");

  private DataChange underTest = new DeletePersonMeasures(db.database());

  @Test
  public void delete_person_measures() throws SQLException {
    insertComponent("P1", "PRJ", "TRK");
    insertComponent("D1", "DIR", "DIR");
    insertComponent("F1", "FIL", "FIL");
    insertComponent("F2", "FIL", "UTS");
    insertSnapshot("S1", "P1", false);
    insertSnapshot("S2", "P1", true);
    // past measures
    long m1 = insertMeasure("P1", "S1");
    long m2 = insertMeasure("D1", "S1");
    long m3 = insertMeasure("F1", "S1");
    long m4 = insertMeasure("F2", "S1");
    long m5 = insertPersonMeasure("P1", "S1");
    long m6 = insertPersonMeasure("F1", "S1");
    // last measures
    long m7 = insertMeasure("P1", "S2");
    long m8 = insertMeasure("D1", "S2");
    long m9 = insertMeasure("F1", "S2");
    long m10 = insertMeasure("F2", "S2");
    long m11 = insertPersonMeasure("P1", "S2");
    long m12 = insertPersonMeasure("F1", "S2");

    underTest.execute();

    assertThat(db.countRowsOfTable("PROJECTS")).isEqualTo(4);
    assertThat(db.countRowsOfTable("SNAPSHOTS")).isEqualTo(2);
    assertThatMeasuresAreExactly(m1, m2, m3, m4, m7, m8, m9, m10);

    // migration is re-entrant
    underTest.execute();
    assertThat(db.countRowsOfTable("PROJECTS")).isEqualTo(4);
    assertThat(db.countRowsOfTable("SNAPSHOTS")).isEqualTo(2);
    assertThatMeasuresAreExactly(m1, m2, m3, m4, m7, m8, m9, m10);
  }

  private void assertThatMeasuresAreExactly(long... expectedMeasureIds) {
    long[] ids = db.select("select id as \"id\" from project_measures")
      .stream()
      .mapToLong(m -> (Long) m.get("id"))
      .toArray();
    assertThat(ids).containsOnly(expectedMeasureIds);
  }

  private void insertComponent(String uuid, String scope, String qualifier) {
    db.executeInsert("PROJECTS",
      "ORGANIZATION_UUID", "O1",
      "KEE", "" + GENERATOR.incrementAndGet(),
      "UUID", uuid,
      "PROJECT_UUID", "" + GENERATOR.incrementAndGet(),
      "MAIN_BRANCH_PROJECT_UUID", "" + GENERATOR.incrementAndGet(),
      "UUID_PATH", ".",
      "ROOT_UUID", "" + GENERATOR.incrementAndGet(),
      "PRIVATE", "true",
      "QUALIFIER", qualifier,
      "SCOPE", scope);
  }

  private void insertSnapshot(String uuid, String projectUuid, boolean last) {
    db.executeInsert("SNAPSHOTS",
      "UUID", uuid,
      "COMPONENT_UUID", projectUuid,
      "STATUS", "P",
      "ISLAST", last);
  }

  private long insertMeasure(String componentUuid, String analysisUuid) {
    long id = GENERATOR.incrementAndGet();
    db.executeInsert("PROJECT_MEASURES",
      "ID", id,
      "METRIC_ID", "42",
      "COMPONENT_UUID", componentUuid,
      "ANALYSIS_UUID", analysisUuid);
    return id;
  }

  private long insertPersonMeasure(String componentUuid, String analysisUuid) {
    long id = GENERATOR.incrementAndGet();
    db.executeInsert("PROJECT_MEASURES",
      "ID", id,
      "METRIC_ID", "42",
      "COMPONENT_UUID", componentUuid,
      "ANALYSIS_UUID", analysisUuid,
      "PERSON_ID", RandomUtils.nextInt(100));
    return id;
  }
}
