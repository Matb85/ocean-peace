<script lang="ts">
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import { Schedule } from "@redinnlabs/system/Units";
  import { TextInput } from "@redinnlabs/system/Form";
  import { Button } from "@redinnlabs/system/Elements";

  import W from "@redinnlabs/system/assets/icon-working.svg";
</script>

<FullHeading backHref="/profile">Settings</FullHeading>

<H thin>Your Devices</H>

<div class="card-flex-col">
  {#each Array(2) as _}
    <Schedule src={W} title="Event example" />
  {/each}
</div>

<H thin>Account Details</H>

<section class="card-flex-col">
  <TextInput label="First Name" placeholder="Your first name" />
  <TextInput label="First Name" placeholder="Your last name" />
  <TextInput label="First Name" placeholder="Your password" />
  <Button>save</Button>
</section>

<H thin>Actions</H>

<section class="card-flex-col w-3/4">
  <Button isFullWidth>Support</Button>
  <Button isFullWidth>Message Us</Button>
  <Button isFullWidth isWarning>Delete Account</Button>
</section>
