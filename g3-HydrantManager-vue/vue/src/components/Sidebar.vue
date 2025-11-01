<template>
  <aside class="sidebar">
    <SearchCard
        :search-query="searchQuery"
        :hydrants="hydrants"
        @update:search-query="$emit('update:search-query', $event)"
        @hydrant-select="$emit('hydrant-select', $event)"
        @search="$emit('search')"
    />
    <FilterCard
        :filter-status="filterStatus"
        :filter-area="filterArea"
        :filter-date="filterDate"
        @update:filter-status="$emit('update:filter-status', $event)"
        @update:filter-area="$emit('update:filter-area', $event)"
        @update:filter-date="$emit('update:filter-date', $event)"
    />
    <StatsCards :stats="stats" />
  </aside>
</template>

<script>
import SearchCard from './SearchCard.vue'
import FilterCard from './FilterCard.vue'
import StatsCards from './StatsCards.vue'

export default {
  name: 'Sidebar',
  components: {
    SearchCard,
    FilterCard,
    StatsCards
  },
  props: {
    searchQuery: String,
    filterStatus: String,
    filterArea: String,
    filterDate: [String, Array],
    selectedHydrant: String,
    hydrants: Array,
    stats: Object
  },
  emits: [
    'update:search-query',
    'update:filter-status',
    'update:filter-area',
    'update:filter-date',
    'hydrant-select',
    'search'
  ]
}
</script>

<style scoped>
.sidebar {
  width: 300px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

@media (max-width: 768px) {
  .sidebar {
    width: 100%;
  }
}
</style>