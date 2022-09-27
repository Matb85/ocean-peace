<script lang="ts">
  import { Button, H } from "@redinnlabs/system/Elements";
  import { TextInput } from "@redinnlabs/system/Form";
  import Wrapper from "$lib/Wrapper.svelte";

  import Link from "$lib/Link.svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { t } from "$lib/i18n";

  let value = "";
  /** sets the username
   * @returns void
   */
  function setUsername() {
    Api.setPreference("name", value);
  }
</script>

<Wrapper>
  <div slot="header" class="text-center">
    <H tag={3}>
      <img src="/logo.svg" alt="logo" class="inline-block h-14" />Ocean Peace
    </H>
    <H thin tag={6} className="mt-0">By RedinnLabs</H>
    <H thin tag={5} className="mt-4 w-3/4 mx-auto">{$t("d.setup.regain")}</H>
  </div>
  <H thin className="mb-4 text-center">{$t("d.setup.name")}</H>
  <TextInput placeholder={$t("d.settings.name")} bind:value />

  <Link
    on:click={setUsername}
    className="absolute bottom-5{value.length > 0 ? '' : ' opacity-50'}"
    href={value.length > 0 ? "/setup/2" : ""}
  >
    <Button>{$t("d.cta.con")}</Button>
  </Link>
</Wrapper>
