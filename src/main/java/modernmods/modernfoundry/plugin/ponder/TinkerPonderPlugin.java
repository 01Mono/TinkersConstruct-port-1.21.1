package modernmods.modernfoundry.plugin.ponder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.PonderStoryBoard;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import modernmods.modernfoundry.TConstruct;

public class TinkerPonderPlugin implements PonderPlugin {
  public static void init() {
    PonderIndex.addPlugin(new TinkerPonderPlugin());
  }

  @Override
  public String getModId() {
    return TConstruct.MOD_ID;
  }

  @Override
  public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
    add(helper, "melter", TinkerPonderPlugin::melter, own("seared_melter", "seared_heater"));
    add(helper, "casting", TinkerPonderPlugin::casting, own("seared_faucet", "scorched_faucet", "seared_table", "scorched_table", "seared_basin", "scorched_basin"));
    add(helper, "smeltery", TinkerPonderPlugin::smeltery, own("smeltery_controller", "seared_drain", "seared_duct", "seared_fuel_tank", "seared_ingot_tank", "seared_fuel_gauge", "seared_ingot_gauge"));
    add(helper, "alloyer", TinkerPonderPlugin::alloyer, own("scorched_alloyer", "scorched_fuel_tank", "scorched_ingot_tank"));
    if (ModList.get().isLoaded("create")) {
      add(helper, "create_mixing", TinkerPonderPlugin::create_mixing, own("seared_melter", "seared_table"));
      add(helper, "create_pump", TinkerPonderPlugin::create_pump, own("seared_fuel_tank", "seared_drain", "seared_duct"));
    }
    if (ModList.get().isLoaded("ae2")) {
      add(helper, "ae2_storage", TinkerPonderPlugin::ae2_storage, own("seared_fuel_tank", "seared_drain", "seared_duct"));
    }
    if (ModList.get().isLoaded("iceandfire")) {
      add(helper, "iaf_fuel", TinkerPonderPlugin::iaf_fuel, ResourceLocation.fromNamespaceAndPath(TConstruct.MOD_ID, "seared_fuel_tank"), ResourceLocation.fromNamespaceAndPath("iceandfire", "fire_dragon_blood"), ResourceLocation.fromNamespaceAndPath("iceandfire", "ice_dragon_blood"), ResourceLocation.fromNamespaceAndPath("iceandfire", "lightning_dragon_blood"));
    }
  }

  private static void add(PonderSceneRegistrationHelper<ResourceLocation> helper, String schematic, PonderStoryBoard board, ResourceLocation... components) {
    for (ResourceLocation component : components) {
      helper.addStoryBoard(component, schematic, board);
    }
  }

  private static ResourceLocation[] own(String... names) {
    ResourceLocation[] result = new ResourceLocation[names.length];
    for (int i = 0; i < names.length; i++) {
      result[i] = ResourceLocation.fromNamespaceAndPath(TConstruct.MOD_ID, names[i]);
    }
    return result;
  }

  private static BlockState block(String name) {
    return BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(TConstruct.MOD_ID, name)).defaultBlockState();
  }

  private static void melter(SceneBuilder scene, SceneBuildingUtil util) {
    scene.title("melter", "Melting items with the Melter");
    scene.configureBasePlate(0, 0, 5);
    scene.showBasePlate();
    scene.idle(10);
    scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
    scene.idle(10);
    scene.world().showSection(util.select().position(2, 2, 2), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(60).text("The Melter is a small furnace that turns items into molten fluid").pointAt(util.vector().centerOf(2, 2, 2)).placeNearTarget();
    scene.idle(70);
    scene.overlay().showText(60).text("It needs heat from directly below: a Heater burning solid fuel works").pointAt(util.vector().centerOf(2, 1, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(70);
    scene.world().setBlock(util.grid().at(2, 1, 2), block("seared_fuel_tank"), true);
    scene.idle(10);
    scene.overlay().showText(60).text("A tank of fuel below it also works, and reaches higher temperatures").pointAt(util.vector().centerOf(2, 1, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(70);
    scene.world().showSection(util.select().position(2, 2, 1), Direction.DOWN);
    scene.idle(10);
    scene.world().showSection(util.select().position(2, 1, 1), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(60).text("Attach a Faucet to pour the result into a Casting Table").pointAt(util.vector().centerOf(2, 2, 1)).placeNearTarget().attachKeyFrame();
    scene.idle(70);
    scene.markAsFinished();
  }

  private static void casting(SceneBuilder scene, SceneBuildingUtil util) {
    scene.title("casting", "Casting with Faucets, Tables and Basins");
    scene.configureBasePlate(0, 0, 5);
    scene.showBasePlate();
    scene.idle(10);
    scene.world().showSection(util.select().fromTo(1, 1, 3, 1, 2, 3), Direction.DOWN);
    scene.idle(10);
    scene.world().showSection(util.select().position(1, 2, 2), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(60).text("A Faucet placed on a tank or drain pours its fluid downward").pointAt(util.vector().centerOf(1, 2, 2)).placeNearTarget();
    scene.idle(70);
    scene.overlay().showControls(util.vector().topOf(1, 2, 2), Pointing.DOWN, 40).rightClick();
    scene.overlay().showText(50).text("Right-click the Faucet to start pouring").pointAt(util.vector().centerOf(1, 2, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(60);
    scene.world().showSection(util.select().position(1, 1, 2), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(70).text("A Casting Table shapes the fluid: use a cast for ingots, nuggets or tool parts").pointAt(util.vector().centerOf(1, 1, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(80);
    scene.world().showSection(util.select().fromTo(3, 1, 3, 3, 2, 3), Direction.DOWN);
    scene.idle(10);
    scene.world().showSection(util.select().position(3, 2, 2), Direction.DOWN);
    scene.idle(10);
    scene.world().showSection(util.select().position(3, 1, 2), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(60).text("A Casting Basin makes full blocks").pointAt(util.vector().centerOf(3, 1, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(70);
    scene.markAsFinished();
  }

  private static void smeltery(SceneBuilder scene, SceneBuildingUtil util) {
    scene.title("smeltery", "Building a Smeltery");
    scene.configureBasePlate(0, 0, 7);
    scene.scaleSceneView(0.8F);
    scene.showBasePlate();
    scene.idle(10);
    scene.world().showSection(util.select().fromTo(2, 1, 2, 4, 1, 4), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(50).text("Start with a floor of seared blocks").pointAt(util.vector().centerOf(3, 1, 3)).placeNearTarget();
    scene.idle(60);
    scene.world().showSection(util.select().fromTo(1, 2, 1, 5, 2, 5), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(60).text("Surround it with walls, leaving the corners empty").pointAt(util.vector().centerOf(3, 2, 1)).placeNearTarget().attachKeyFrame();
    scene.idle(70);
    scene.overlay().showText(60).text("The Controller goes in the wall and checks the structure").pointAt(util.vector().centerOf(3, 2, 5)).placeNearTarget().attachKeyFrame();
    scene.idle(70);
    scene.overlay().showText(60).text("Fuel tanks in the wall heat the Smeltery").pointAt(util.vector().centerOf(5, 2, 3)).placeNearTarget().attachKeyFrame();
    scene.idle(70);
    scene.world().showSection(util.select().fromTo(0, 1, 3, 0, 2, 3), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(60).text("A Drain lets fluid out, here through a Faucet into a table").pointAt(util.vector().centerOf(1, 2, 3)).placeNearTarget().attachKeyFrame();
    scene.idle(70);
    scene.markAsFinished();
  }

  private static void alloyer(SceneBuilder scene, SceneBuildingUtil util) {
    scene.title("alloyer", "Mixing alloys with the Alloyer");
    scene.configureBasePlate(0, 0, 5);
    scene.showBasePlate();
    scene.idle(10);
    scene.world().showSection(util.select().position(2, 2, 2), Direction.DOWN);
    scene.idle(10);
    scene.world().showSection(util.select().fromTo(1, 1, 2, 1, 2, 2), Direction.DOWN);
    scene.idle(10);
    scene.world().showSection(util.select().fromTo(3, 1, 2, 3, 2, 2), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(60).text("The Alloyer mixes fluids from the tanks around and above it").pointAt(util.vector().centerOf(2, 2, 2)).placeNearTarget();
    scene.idle(70);
    scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(50).text("It burns fuel from a tank directly below").pointAt(util.vector().centerOf(2, 1, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(60);
    scene.overlay().showText(50).text("Take the finished alloy out with a Faucet").pointAt(util.vector().centerOf(2, 2, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(60);
    scene.markAsFinished();
  }

  private static void create_mixing(SceneBuilder scene, SceneBuildingUtil util) {
    scene.title("create_mixing", "Melting metals with Create");
    scene.configureBasePlate(0, 0, 5);
    scene.showBasePlate();
    scene.idle(10);
    scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
    scene.idle(10);
    scene.world().showSection(util.select().position(2, 2, 2), Direction.DOWN);
    scene.idle(10);
    scene.world().showSection(util.select().position(2, 4, 2), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(70).text("With Create installed, a Mechanical Mixer can melt metals in a Basin").pointAt(util.vector().centerOf(2, 4, 2)).placeNearTarget();
    scene.idle(80);
    scene.overlay().showText(70).text("Most metals need a heated Blaze Burner; harder ones need it superheated").pointAt(util.vector().centerOf(2, 1, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(80);
    scene.overlay().showText(60).text("It also mixes molten alloys and fires grout into bricks").pointAt(util.vector().centerOf(2, 2, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(70);
    scene.overlay().showText(70).text("A Mechanical Press compacts 90 mB of molten metal into an ingot").attachKeyFrame();
    scene.idle(80);
    scene.markAsFinished();
  }

  private static void create_pump(SceneBuilder scene, SceneBuildingUtil util) {
    scene.title("create_pump", "Moving Tinkers fluids with Create");
    scene.configureBasePlate(0, 0, 5);
    scene.showBasePlate();
    scene.idle(10);
    scene.world().showSection(util.select().fromTo(1, 1, 2, 3, 1, 2), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(70).text("Tanks, drains and ducts connect directly to Create pipes and pumps").pointAt(util.vector().centerOf(2, 1, 2)).placeNearTarget();
    scene.idle(80);
    scene.overlay().showText(60).text("Fluids can flow both ways between the two mods").pointAt(util.vector().centerOf(3, 1, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(70);
    scene.markAsFinished();
  }

  private static void ae2_storage(SceneBuilder scene, SceneBuildingUtil util) {
    scene.title("ae2_storage", "Linking Tinkers fluids to AE2");
    scene.configureBasePlate(0, 0, 5);
    scene.showBasePlate();
    scene.idle(10);
    scene.world().showSection(util.select().fromTo(1, 1, 2, 2, 1, 2), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(70).text("Tanks, drains and ducts expose their fluids to Applied Energistics 2").pointAt(util.vector().centerOf(1, 1, 2)).placeNearTarget();
    scene.idle(80);
    scene.overlay().showText(70).text("Point a Storage Bus at a tank to see its fluid in your ME network").pointAt(util.vector().centerOf(2, 1, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(80);
    scene.overlay().showText(60).text("Import and Export Buses can also move fluid in and out").pointAt(util.vector().centerOf(2, 1, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(70);
    scene.markAsFinished();
  }

  private static void iaf_fuel(SceneBuilder scene, SceneBuildingUtil util) {
    scene.title("iaf_fuel", "Dragon blood as fuel");
    scene.configureBasePlate(0, 0, 5);
    scene.showBasePlate();
    scene.idle(10);
    scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
    scene.idle(10);
    scene.world().showSection(util.select().position(2, 2, 2), Direction.DOWN);
    scene.idle(10);
    scene.overlay().showText(50).text("Ice and Fire dragon blood melts into a fluid").pointAt(util.vector().centerOf(2, 1, 2)).placeNearTarget();
    scene.idle(60);
    scene.overlay().showText(70).text("Stored in a fuel tank, it burns at 2500, far hotter than lava").pointAt(util.vector().centerOf(2, 1, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(80);
    scene.overlay().showText(60).text("Use it to melt Dragonsteel and other demanding metals").pointAt(util.vector().centerOf(2, 2, 2)).placeNearTarget().attachKeyFrame();
    scene.idle(70);
    scene.markAsFinished();
  }
}
