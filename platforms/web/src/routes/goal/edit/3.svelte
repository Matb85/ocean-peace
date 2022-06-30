<script lang="ts">
  import { Button, Icon } from "@redinnlabs/system/Elements";
  import { mdiCheck } from "@mdi/js";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";

  import { onMount } from "svelte";
  import Api from "$api";
  import type { AppIconI } from "$lib/../../api/index";
  import { fly } from "svelte/transition";
  import { goto } from "$app/navigation";

  let selectedApps: AppIconI[] = [];

  onMount(async () => {
    selectedApps = await Api.getAppIcons(JSON.parse(sessionStorage.getItem("edit_goal_apps")));
  });

  let isComplete = false;
</script>

<FullHeading backHref="./2">Summary</FullHeading>

<H tag={6} thin>Goal name</H>
<H tag={4} className="-mt-2" thin>{sessionStorage.getItem("edit_goal_name")}</H>

<H tag={6} thin>Active Days</H>
<div class="flex flex-wrap justify-center gap-2">
  {#each JSON.parse(sessionStorage.getItem("edit_goal_active_days")) as day}
    <Button size="small">{day}</Button>
  {/each}
</div>

<H tag={6} thin>Limit</H>
<div class="flex flex-wrap justify-center gap-2 items-center">
  <H tag={4} className="mt-0 mb-0" thin>
    {sessionStorage.getItem("edit_goal_time_hours")}h
    {parseInt(sessionStorage.getItem("edit_goal_time_minutes")) * 5}min
  </H>
  <!---->
  <Button size="small">Time Period</Button>
</div>

<H tag={6} thin>Limit type</H>
<Button size="small">{sessionStorage.getItem("edit_goal_limit_type")}</Button>

<H tag={6} thin>Selected apps</H>
<SelectedApps apps={selectedApps} />

<H tag={6} thin>Selected Websites</H>

<div
  on:click={() => {
    isComplete = true;
    setTimeout(() => {
      goto("/");
    }, 1500);
  }}
  class="fixed-bottom-button"
  href="/"
>
  <Button isFullWidth>save</Button>
</div>

{#if isComplete}
  <section in:fly={{ y: 200, duration: 300 }} class="fixed top-0 left-0 z-50 bg-white w-full h-screen">
    <H tag={3} thin className="mt-32">Goal saved!</H>
    <section
      in:fly={{ x: -400, duration: 1200 }}
      class="mt-16 mx-auto bg-green-light w-1/2 aspect-square rounded-3xl flex justify-center items-center"
    >
      <Icon d={mdiCheck} className="fill-green-dark w-3/4" />
    </section>
  </section>
{/if}
