<script lang="ts">
  import FullHeading from "$lib/FullHeading.svelte";

  import { TextInput } from "@redinnlabs/system/Form";
  import { Button, H } from "@redinnlabs/system/Elements";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { TimeInput, timeInputConfig } from "@redinnlabs/system/Form";
  import type { columnI } from "@redinnlabs/system/Form/TimeInput/TimeInput.svelte";
  import LanguageSelector from "$lib/LanguageSelector.svelte";
  import { timeFromNumber, numberFromTime } from "$lib/utils";
  import type { PreferencesI } from "$schema/ui";
  import { t } from "$lib/i18n";

  const hours: columnI = {
    id: "hours",
    units: "h",
    data: [...Array(6).keys()],
    current: 2,
    multiplier: 1,
  };
  const minutes = timeInputConfig.minutesConfig("minutes", 15 / 5);
  /** user name binding */
  let name = "";
  /** track changes */
  const data: PreferencesI = JSON.parse(sessionStorage.getItem("preferences_temp") || "{}");
  let baseName = (name = data.name);
  let firstTime = parseInt(data.screentime);
  const time = timeFromNumber(data.screentime);
  hours.current = time[0];
  minutes.current = time[1] / 5;

  let h: number = hours.current;
  let m: number = minutes.current;
  /** updates the time
   * @param e event
   * @returns void
   */
  function onUpdate(e: CustomEvent<{ id: string; current: number }>) {
    const { id, current } = e.detail;
    if (id == "hours") h = current;
    if (id == "minutes") m = current;
  }
  /** updates the preferences
   * @returns void
   */
  function save() {
    Api.setPreference("name", name);
    Api.setPreference("screentime", numberFromTime([hours.current, minutes.current * 5]) + "");
    baseName = name;
    firstTime = numberFromTime([hours.current, minutes.current * 5]);
  }
</script>

<FullHeading backHref="/profile">{$t("d.settings.settings")}</FullHeading>

<H thin>{$t("d.settings.name")}</H>

<section class="card-flex-col">
  <TextInput label={$t("d.settings.name")} placeholder={$t("d.settings.name")} bind:value={name} />

  <H thin>{$t("d.settings.screentime")}</H>
  <TimeInput columns={[hours, minutes]} on:update={onUpdate} />
  <H thin>{$t("d.settings.lang")}</H>

  <LanguageSelector className="text-black" />
</section>
<div class="fixed-bottom-button{baseName == name && numberFromTime([h, m * 5]) == firstTime ? ' grayscale' : ''}">
  <Button isFullWidth on:click={save}>{$t("d.cta.save")}</Button>
</div>
