.file "tests/codeGenOfMe/if_08.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.LC1:
.string "the world is wrong\n"
.LC2:
.string "it is ok\n"
.LC3:
.string "it is wrong\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $7,-16(%rbp)
movb $1,-20(%rbp)
movb $0,-19(%rbp)
movb $1,-18(%rbp)
movb $0,-17(%rbp)
movb -20(%rbp),%al
cmpb $0,%al
jle .L1
movb -19(%rbp),%al
cmpb $0,%al
jle .L2
movb -17(%rbp),%al
cmpb $0,%al
jle .L1
movb -18(%rbp),%al
cmpb $0,%al
jle .L1
.L2:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L3
.L1:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L3:
movq $3,%rax
movq $4,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rax
movq -16(%rbp),%r10
cmpq %r10,%rax
jne .L4
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L5
.L4:
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L5:
leave
ret
jmp .L1
jmp .L2
jmp .L1

