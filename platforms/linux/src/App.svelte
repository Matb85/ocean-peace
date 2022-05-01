<script lang="ts">
  import { emit, listen } from '@tauri-apps/api/event'

  import MainPage from './MainPage.svelte';

  let currentActiveView: string = 'MainPage';

  const unlisten = listen('ChangeView', event => {
    currentActiveView = event.payload as string;
  })
</script>

<main>
  {#if currentActiveView == 'MainPage'}
    <MainPage/> 
  {/if}
</main>

<style>
  
</style>
