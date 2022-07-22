import { registerPlugin } from "@capacitor/core";
import type { PresetI, PresetsMethods } from "@redinn/oceanpeace-web/api/presets";

interface PresetsPlugin {
  getAllPresets(): Promise<{ presets: PresetI[] }>;
  getPreset(data: { id: string }): Promise<{ preset: PresetI }>;
  savePreset(data: { data: PresetI }): void;
  deletePreset(data: { id: string }): void;
}

const Presets = registerPlugin<PresetsPlugin>("Presets");

const plugin: PresetsMethods = {
  async getAllPresets(): Promise<PresetI[]> {
    console.log("hi");
    const { presets } = await Presets.getAllPresets();
    return presets;
  },
  async getPreset(id: string): Promise<PresetI | null> {
    const { preset } = await Presets.getPreset({ id });
    return preset;
  },
  async savePreset(data: PresetI): Promise<void> {
    await Presets.savePreset({ data });
  },
  async deletePreset(id: string): Promise<void> {
    await Presets.deletePreset({ id });
  },
};

export default plugin;
