<script lang="ts">
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import { TextInput } from "@redinnlabs/system/Form";
  import { Button } from "@redinnlabs/system/Elements";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { TimeInput, timeInputConfig } from "@redinnlabs/system/Form";
  import type { columnI } from "@redinnlabs/system/Form/TimeInput/TimeInput.svelte";
  import LanguageSelector from "$lib/LanguageSelector.svelte";
  import { timeFromNumber, numberFromTime } from "$lib/utils";
  import { t } from "$lib/i18n";
  const hours: columnI = {
    units: "h",
    data: [...Array(6).keys()],
    current: 2,
    multiplier: 1,
  };
  const minutes = timeInputConfig.minutesConfig(15 / 5);
  let h: number;
  let m: number;
  h = hours.current;
  m = minutes.current;
  Object.defineProperty(hours, "current", {
    set: val => {
      h = val;
    },
    get: () => h,
  });
  Object.defineProperty(minutes, "current", {
    set: val => {
      m = val;
    },
    get: () => m,
  });

  let name = "";
  let baseName = "";
  let firstTime = 0;
  /** updates the preferences
   * @returns {void}
   */
  function save() {
    Api.setPreference("name", name);
    Api.setPreference("screentime", numberFromTime([h, m * 5]) + "");
    baseName = name;
    firstTime = numberFromTime([h, m * 5]);
  }
  // !TODO fix the compliance bugs due to asynchronous data fetching!
  Api.getPreferences().then(data => {
    baseName = name = data.name;
    firstTime = parseInt(data.screentime);
    const time = timeFromNumber(data.screentime);
    h = time[0];
    m = time[1] / 5;
  });
</script>

<FullHeading backHref="/profile">{$t("d.settings.settings")}</FullHeading>

<H thin>{$t("d.settings.name")}</H>

<section class="card-flex-col">
  <TextInput label={$t("d.settings.name")} placeholder={$t("d.settings.name")} bind:value={name} />

  <H thin>{$t("d.settings.screentime")}</H>
  <TimeInput columns={[hours, minutes]} />
  <H thin>{$t("d.settings.lang")}</H>

  <LanguageSelector className="text-black" />
</section>
{h}{m}
<div class="fixed-bottom-button{baseName == name && numberFromTime([h, m * 5]) == firstTime ? ' grayscale' : ''}">
  <Button isFullWidth on:click={save}>{$t("d.cta.save")}</Button>
</div>
