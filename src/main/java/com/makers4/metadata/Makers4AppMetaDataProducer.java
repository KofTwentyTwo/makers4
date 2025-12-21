package com.makers4.metadata;


import com.kingsrook.qqq.backend.core.model.metadata.MetaDataProducerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QAppMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QAppSection;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QIcon;
import com.makers4.model.Cabinet;
import com.makers4.model.CabinetOpening;
import com.makers4.model.CutList;
import com.makers4.model.CutListItem;
import com.makers4.model.Part;
import com.makers4.model.Project;
import com.makers4.model.RenderArtifact;
import com.makers4.model.RenderJob;
import com.makers4.model.User;
import com.makers4.model.lookup.CabinetType;
import com.makers4.model.lookup.DoorPanelStyle;
import com.makers4.model.lookup.DoorStyle;
import com.makers4.model.lookup.DrawerFrontStyle;
import com.makers4.model.lookup.DrawerSlideType;
import com.makers4.model.lookup.EdgeProfile;
import com.makers4.model.lookup.FaceType;
import com.makers4.model.lookup.FinishedEndStyle;
import com.makers4.model.lookup.Material;
import com.makers4.model.lookup.MaterialType;
import com.makers4.model.lookup.OpeningType;
import com.makers4.model.lookup.PartType;
import com.makers4.model.lookup.ToeKickStyle;
import com.makers4.model.lookup.UnitSystem;
import java.util.List;


public class Makers4AppMetaDataProducer implements MetaDataProducerInterface<QAppMetaData>
{
   @Override
   public QAppMetaData produce(QInstance qInstance)
   {
      ////////////////////////////////////////
      // Register additional apps directly  //
      ////////////////////////////////////////
      qInstance.addApp(createSetupApp(qInstance));
      qInstance.addApp(createPrintsApp(qInstance));
      qInstance.addApp(createAdminApp(qInstance));

      ////////////////////////////////
      // Return the main Projects app //
      ////////////////////////////////
      return createProjectsApp(qInstance);
   }



   private QAppMetaData createProjectsApp(QInstance qInstance)
   {
      return new QAppMetaData()
         .withName("Projects")
         .withIcon(new QIcon().withName("folder"))
         .withSortOrder(100)
         .withSections(List.of(
            new QAppSection()
               .withName("projects")
               .withLabel("Projects")
               .withIcon(new QIcon().withName("folder"))
               .withTables(List.of(Project.TABLE_NAME, Cabinet.TABLE_NAME, CabinetOpening.TABLE_NAME, Part.TABLE_NAME))
         ));
   }



   private QAppMetaData createSetupApp(QInstance qInstance)
   {
      return new QAppMetaData()
         .withName("Setup")
         .withIcon(new QIcon().withName("settings"))
         .withSortOrder(200)
         .withSections(List.of(
            new QAppSection()
               .withName("materials")
               .withLabel("Materials")
               .withIcon(new QIcon().withName("layers"))
               .withTables(List.of(Material.TABLE_NAME, MaterialType.TABLE_NAME)),
            new QAppSection()
               .withName("cabinet-styles")
               .withLabel("Cabinet Styles")
               .withIcon(new QIcon().withName("kitchen"))
               .withTables(List.of(CabinetType.TABLE_NAME, FaceType.TABLE_NAME, ToeKickStyle.TABLE_NAME, FinishedEndStyle.TABLE_NAME)),
            new QAppSection()
               .withName("openings")
               .withLabel("Openings & Doors")
               .withIcon(new QIcon().withName("door_front"))
               .withTables(List.of(OpeningType.TABLE_NAME, DoorStyle.TABLE_NAME, DoorPanelStyle.TABLE_NAME)),
            new QAppSection()
               .withName("drawers")
               .withLabel("Drawers")
               .withIcon(new QIcon().withName("inbox"))
               .withTables(List.of(DrawerFrontStyle.TABLE_NAME, DrawerSlideType.TABLE_NAME)),
            new QAppSection()
               .withName("other")
               .withLabel("Other")
               .withIcon(new QIcon().withName("more_horiz"))
               .withTables(List.of(EdgeProfile.TABLE_NAME, PartType.TABLE_NAME, UnitSystem.TABLE_NAME))
         ));
   }



   private QAppMetaData createPrintsApp(QInstance qInstance)
   {
      return new QAppMetaData()
         .withName("Prints")
         .withIcon(new QIcon().withName("print"))
         .withSortOrder(300)
         .withSections(List.of(
            new QAppSection()
               .withName("cut-lists")
               .withLabel("Cut Lists")
               .withIcon(new QIcon().withName("content_cut"))
               .withTables(List.of(CutList.TABLE_NAME, CutListItem.TABLE_NAME)),
            new QAppSection()
               .withName("renders")
               .withLabel("Renders")
               .withIcon(new QIcon().withName("image"))
               .withTables(List.of(RenderJob.TABLE_NAME, RenderArtifact.TABLE_NAME))
         ));
   }



   private QAppMetaData createAdminApp(QInstance qInstance)
   {
      return new QAppMetaData()
         .withName("Admin")
         .withIcon(new QIcon().withName("admin_panel_settings"))
         .withSortOrder(400)
         .withSections(List.of(
            new QAppSection()
               .withName("users")
               .withLabel("Users")
               .withIcon(new QIcon().withName("people"))
               .withTables(List.of(User.TABLE_NAME))
         ));
   }
}
