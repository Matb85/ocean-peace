<script lang="ts">
  import { goTo } from "$lib/utils";
  export let className = "";
  export let href = "";
  /** forces a page traqnsition before changing the page
   * @param e click event
   * @returns nothing
   */
  function navigate(e: Event) {
    e.preventDefault();
    goTo("#" + href);
  }
</script>

<a href="#{href}" on:click={navigate} on:click class={className}><slot /></a>
