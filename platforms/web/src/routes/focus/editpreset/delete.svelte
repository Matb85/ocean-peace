<script lang="ts">
  import Delete from "$lib/Delete.svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { goTo } from "$lib/utils";
  import SM from "$lib/sessionManager";
  SM.action.setProp("nativeBackUrl", "/focus/editpreset/delete");

  const data = SM.preset.getProps("id", "name");

  /** delete a given preset
   * @returns void
   */
  async function deletePreset() {
    await Api.deletePreset(data.id);
    goTo("/focus");
  }
</script>

<Delete cancelHref="/focus/preset?id={data.id}" type="preset" name={data.name} callback={deletePreset} />
