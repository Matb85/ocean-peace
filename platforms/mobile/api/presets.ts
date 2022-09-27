import { registerPlugin } from "@capacitor/core";
import type { PresetI, PresetsMethods } from "@redinn/oceanpeace-web/api/presets";

interface PresetsPlugin {
  getAllPresets(): Promise<{ presets: PresetI[] }>;
  getPreset(data: { id: string }): Promise<{ preset: PresetI }>;
  savePreset(data: { data: PresetI }): Promise<void>;
  deletePreset(data: { id: string }): Promise<void>;
}

const Presets = registerPlugin<PresetsPlugin>("Presets");

const plugin: PresetsMethods = {
  async getAllPresets(): Promise<PresetI[]> {
    const { presets } = await Presets.getAllPresets();
    return presets;
  },
  async getPreset(id: string): Promise<PresetI | null> {
    const { preset } = await Presets.getPreset({ id });
    return preset;
  },
  savePreset(data: PresetI): Promise<void> {
    return Presets.savePreset({ data });
  },
  deletePreset(id: string): Promise<void> {
    return Presets.deletePreset({ id });
  },
};

export default plugin;
