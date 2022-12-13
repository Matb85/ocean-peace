<script>
  import { IconButton } from "@redinnlabs/system/Elements";
  import { mdiAccountOutline } from "@mdi/js";
  import { t } from "./i18n";
  import { Button } from "@redinnlabs/system/Elements";
  import Link from "./Link.svelte";
</script>

<nav class="fixed bottom-0 w-full p-2 flex justify-center gap-2">
  <Link href="/profile" className="flex-shrink-0 bg-white">
    <IconButton secondary d={mdiAccountOutline} isSquare />
  </Link>
  <Link href="/focus" className="w-full flex-shrink">
    <Button isFullWidth>{$t("d.focus.start_session")}</Button>
  </Link>
</nav>
