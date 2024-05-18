import React from 'react'
import { DocsThemeConfig } from 'nextra-theme-docs'

const config: DocsThemeConfig = {
  logo: <span>Tech Reborn</span>,
  project: {
    link: 'https://github.com/TechReborn/TechReborn/',
  },
  chat: {
    link: 'https://discord.gg/teamreborn',
  },
  docsRepositoryBase: 'https://github.com/TechReborn/TechReborn/tree/main/docs',
  footer: {
    text: (
      <a href="https://nextra.site" target="_blank">
        Built with Nextra
      </a>
    ),
  },
  useNextSeoProps() {
    return {
      titleTemplate: '%s - Tech Reborn'
    }
  },
  navigation: {
    prev: true,
    next: true,
  },
  sidebar: {
    defaultMenuCollapseLevel: 1,
  }
}

export default config
