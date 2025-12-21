package com.makers4;


import static org.assertj.core.api.Assertions.assertThat;

import com.makers4.metadata.Makers4MetaDataProvider;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import org.junit.jupiter.api.Test;


class MetadataSmokeTest
{
   @Test
   void shouldLoadTablesAndProcesses() throws Exception
   {
      QInstance instance = Makers4MetaDataProvider.defineTestInstance();

      // Core entities
      assertThat(instance.getTables().keySet())
         .contains("user", "project", "cabinet", "cabinet_opening", "part");

      // Lookup tables
      assertThat(instance.getTables().keySet())
         .contains("material", "material_type", "cabinet_type", "face_type", "unit_system");

      // Output tables
      assertThat(instance.getTables().keySet())
         .contains("cut_list", "cut_list_item", "render_job", "render_artifact");
   }
}

