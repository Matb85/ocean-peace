<!-- allows to set the name and select apps & websites -->
<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TextInput } from "@redinnlabs/system/Form";
  import { Preset } from "@redinnlabs/system/Units";
  import Link from "$lib/Link.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import { t } from "$lib/i18n";
  import { onMount } from "svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import type { PresetI } from "$schema";
  import SM from "$lib/sessionManager";

  let name = SM.schedule.getProp("name");
  $: SM.schedule.setProp("name", name);

  let preset = SM.schedule.getProp("preset");
  $: SM.schedule.setProp("preset", preset);

  let presets: PresetI[] = [];
  onMount(async () => {
    presets = await Api.getAllPresets();
  });
</script>

<FullHeading backHref="/dialogs/cancel">
  {$t("d.schedule." + SM.action.getProp("type"))}
</FullHeading>

<H thin>{$t("d.schedule.name")}</H>
<div class="w-11/12">
  <TextInput placeholder={$t("d.schedule.name")} bind:value={name} />
</div>

<H thin>{$t("d.schedule.choose_preset")}</H>

<div class="flex flex-wrap gap-4 justify-center">
  {#each presets as p}
    <Preset
      src={p.icon}
      label={p.name}
      isSelected={preset == p.id}
      on:click={() => {
        preset = p.id;
      }}
    />
  {/each}
</div>

<div class="fixed-bottom-button bg-white" style:opacity={preset.length > 0 && name.length > 0 ? "1" : "0.5"}>
  <Link href={preset.length > 0 && name.length > 0 ? "/focus/editschedule/2" : ""}>
    <Button isFullWidth>{$t("d.cta.con")}</Button>
  </Link>
</div>
