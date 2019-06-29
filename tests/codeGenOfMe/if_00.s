.file "tests/codeGenOfMe/if_00.dcf"
.text
.section .rodata
.LC0:
.string "a\n"
.LC1:
.string "b\n"
.LC2:
.string "c\n"
.LC3:
.string "d\n"
.LC4:
.string "end\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $5,-8(%rbp)
movq -8(%rbp),%rax
cmpq $4,%rax
jle .L1
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L2
.L1:
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L2:
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

