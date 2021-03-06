package tk.burdukowsky.pillOrganizerApi.pill

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("pills")
class PillController(private val pillRepository: PillRepository) {

    @GetMapping("")
    fun getPills(): ResponseEntity<List<Pill>> {
        return ResponseEntity.ok().body(pillRepository.findAll())
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PostMapping("")
    fun createPill(@Valid @RequestBody pill: Pill): ResponseEntity<Pill> {
        pill.id = 0
        val createdPill = this.pillRepository.save(pill)
        return ResponseEntity.ok().body(createdPill)
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PutMapping("/{pillId}")
    fun updatePill(@PathVariable(value = "pillId") pillId: Long,
                   @Valid @RequestBody pill: Pill): ResponseEntity<Pill> {
        if (!this.pillRepository.existsById(pillId)) {
            return ResponseEntity.notFound().build()
        }
        pill.id = pillId
        val updatedPill = this.pillRepository.save(pill)
        return ResponseEntity.ok().body(updatedPill)
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @DeleteMapping("/{pillId}")
    fun deletePill(@PathVariable(value = "pillId") pillId: Long): ResponseEntity<Void> {
        if (!this.pillRepository.existsById(pillId)) {
            return ResponseEntity.notFound().build()
        }
        this.pillRepository.deleteById(pillId)
        return ResponseEntity.ok().build()
    }
}
