<script>
  import { Heading as H, Icon } from "@redinnlabs/system/Elements";
  import { mdiSlack, mdiHomeVariantOutline, mdiAccountOutline } from "@mdi/js";
  import { svgGradients } from "@redinnlabs/system/utils";
  import { t } from "./i18n";
  import Link from "./Link.svelte";
</script>

<nav class="fixed bottom-0 w-full bg-white h-12 shadow-md border-t-2 flex justify-around items-center">
  {#each [{ name: $t("d.nav.profile"), icon: mdiAccountOutline, href: "/profile" }, { name: $t("d.nav.home"), icon: mdiHomeVariantOutline, href: "/" }, { name: $t("d.nav.focus"), icon: mdiSlack, href: "/focus" }] as link}
    <Link href={link.href} className="flex items-center gap-1">
      <Icon className="w-6" d={link.icon} fill={svgGradients.primary} />
      <H tag={6}>{link.name}</H>
    </Link>
  {/each}
</nav>
