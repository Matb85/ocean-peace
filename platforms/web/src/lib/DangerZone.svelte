<script lang="ts">
  export let deleteUrl = "";
  export let label = "";
  import { Button, H } from "@redinnlabs/system/Elements";

  import Link from "$lib/Link.svelte";
  import { t } from "$lib/i18n";
</script>

<H className="!mt-32" thin>{$t("d.danger.zone")}</H>
<hr class="border-0 border-b-2 border-gray w-9/12" />

<Link href={deleteUrl} className="my-4">
  <Button isWarning>{label}</Button>
</Link>

<hr class="border-0 border-b-2 border-gray w-9/12 mb-16" />
