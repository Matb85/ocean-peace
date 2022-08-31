<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import FullHeading from "$lib/FullHeading.svelte";
  import { SearchBar, Schedule } from "@redinnlabs/system/Units";
  import { CheckBox } from "@redinnlabs/system/Form";
  import SM from "$lib/sessionManager";
  import H from "$lib/H.svelte";
  import { mdiChevronRight } from "@mdi/js";
  import { WebsiteTypes } from "$lib/utils/index";
  const goBackUrl = SM.dialogs.backUrl;

  interface WebsiteI {
    url: string;
    label: string;
    favicon: string;
    type: number;
  }
  let selectedWebsites: WebsiteI[] = JSON.parse(SM.dialogs.websites || "[]");
  $: selectedWebsites, (SM.dialogs.websites = JSON.stringify(selectedWebsites));

  let favicon = "/globe.png";
  let url = "";
  $: url, search();

  let urls = [];
  /** fetches the favicon
   * @returns {void}
   */
  async function search() {
    url = url.trim();
    urls = [url, "*." + url, url.slice(0, url.lastIndexOf(".")) + ".*"];
    try {
      await fetch(`https://${url}/favicon.ico`, { mode: "no-cors" });
      favicon = `https://${url}/favicon.ico`;
    } catch {
      resetFavicon();
    }
  }
  /** resets the favicon
   * @returns {void}
   */
  function resetFavicon() {
    favicon = "/globe.png";
  }
</script>

<FullHeading backHref={goBackUrl}>Websites</FullHeading>
<div class="w-11/12 mt-4">
  <SearchBar placeholder="eg. example.com" bind:value={url} icon={mdiChevronRight} />
</div>

{#if url.length > 0}
  <div class="mt-8 card-flex-col">
    {#each urls as label, i}
      <Schedule src={favicon} title={label} alt="" info={WebsiteTypes[i]}>
        <CheckBox
          on:click={() => {
            selectedWebsites = [...selectedWebsites, { url, label, favicon, type: i }];
            url = "";
            resetFavicon();
          }}
        />
      </Schedule>
    {/each}
  </div>
{/if}

<H thin>Selected Websites ({selectedWebsites.length})</H>
{#if url.length > 0}
  <p>start typing to receive results...</p>
{/if}

<div class="card-flex-col">
  {#each selectedWebsites as web}
    <Schedule src={web.favicon} title={web.label} info={WebsiteTypes[web.type]} alt="">
      <CheckBox
        state={true}
        on:click={() => {
          selectedWebsites = selectedWebsites.filter(x => x.label != web.label);
        }}
      />
    </Schedule>
  {/each}
</div>

<a href={goBackUrl} sveltekit:prefetch class="fixed bottom-10 z-50 w-10/12">
  <Button isFullWidth>Save</Button>
</a>
