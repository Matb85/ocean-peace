<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TextInput } from "@redinnlabs/system/Form";
  import Wrapper from "$lib/Wrapper.svelte";
  import H from "$lib/H.svelte";
  import Link from "$lib/Link.svelte";
  import Api from "@redinn/oceanpeace-mobile/api";

  let value = "";
  /** sets the username
   * @returns {void}
   */
  function setUsername() {
    Api.setPreference("name", value);
  }
</script>

<Wrapper>
  <div slot="header">
    <H tag={3}>
      <img src="/logo.svg" alt="logo" class="inline-block h-14" />Ocean Peace
    </H>
    <H thin tag={6} className="mt-0">By RedinnLabs</H>
    <H thin tag={5} className="mt-4 w-3/4 mx-auto">Regain the control of your time.</H>
  </div>
  <H thin className="mb-4">What's your name?</H>
  <TextInput placeholder="Your name" bind:value />

  <Link
    on:click={setUsername}
    className="absolute bottom-5{value.length > 0 ? '' : ' opacity-50'}"
    href={value.length > 0 ? "/setup/2" : ""}
  >
    <Button>Continue</Button>
  </Link>
</Wrapper>
