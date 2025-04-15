<template>
  <div v-if="isVisible" class="modal-overlay">
    <div class="modal-content">
      <h5 class="modal-title">{{ title }}</h5>
      <p>Are you sure you want to delete {{ itemType }} <strong>{{ itemName }}</strong>?</p>
      <div class="modal-footer">
        <button class="btn btn-secondary" @click="closeModal">Cancel</button>
        <button class="btn btn-danger" @click="confirmAction">Yes, Delete</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    title: { type: String, default: "Delete Confirmation" },
    itemType: { type: String, required: true },
    itemName: { type: String, required: true },
    itemId: { type: Number, required: true }
  },
  data() {
    return {
      isVisible: false
    };
  },
  methods: {
    openModal() {
      this.isVisible = true;
    },
    closeModal() {
      this.isVisible = false;
    },
    confirmAction() {
      console.log(`✅ Emitting confirmed event for ${this.itemType} with ID:`, this.itemId);
      this.$emit("confirmed", this.itemId); // ✅ Ensure correct emission
      this.closeModal();
    }
  }
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1050;
}
.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}
.modal-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
</style>
